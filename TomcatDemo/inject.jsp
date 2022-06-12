<%@ page import="java.io.*,org.apache.tomcat.util.codec.binary.Base64" %>
<%
try{
  String b64String = request.getParameter("data").replace(" ", "+");
  System.out.println(b64String);
  byte[] input = Base64.decodeBase64(b64String);

  ByteArrayInputStream is = new ByteArrayInputStream(input);
  java.io.ObjectInputStream inputStream = new java.io.ObjectInputStream(is);
  inputStream.readObject();
}catch(java.io.IOException e){
  System.out.println(e);
}
%>
