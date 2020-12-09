call mvn clean package -Dwildfly.bootable.hollow=true -P package.war,package.wildfly.bootable.jsf
set cyk.variable.system.proxy.uniform.resource.identifier=http://localhost:8081/api/
set cyk.variable.user.interface.theme.menu.is.dynamic=false
set cyk.variable.system.security.authentication.enable=false
call java -jar target\ROOT-bootable.jar --deployment=target\ROOT.war -Djboss.http.port=8082