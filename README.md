# Red Hat Integration Lab

Repositorio con una una ruta de integración camel de ejemplo para desplegar en Openshift Container Platform versión 3.11 con S2I.

## Descripción de funcionalidad

Esta ruta de integración expone un servicio Rest que sirve como Gateway hacia otro microservicio en SpringBoot, el request recibe un número de cuenta y devuelve el saldo de la cuenta.

## URL

> Reemplazar **{HOST_OCP}** por el host de ruta generada en el ambiente de OCP desplegado

```
http://**{HOST_OCP}**/api/consultas/consultar-saldo
```

## Request

```JSON
{
    "numeroCuenta": "12222"
}
```

## Response

```JSON
{
    "saldo": 2300000,
    "fecha": "2020-11-18T20:12:57.079+00:00"
}
```

## Pasos de instalación de OCP

A continuación se describen los pasos para realizar el despliegue de esta ruta de integración en un ambiente de OCP 3.11 o 4.x, una vez se haya clonado este repositorio

> Ubicarse en el proyecto

```
cd redhat-integration-lab
```

> Compilar y generar el Jar
```
mvn clean package -DskipTests=true
```

> Autenticarse en OpenShift
```
oc login --server=https://{{HOST_CONSOLE_OCP}}}:{{PORT_CONSOLE_OCP}} --username={{USER_OCP}} --password={{PASSWORD_OCP}} --insecure-skip-tls-verify=true
```

> Crear el proyecto donde se van a desplegar las aplicaciones
```
oc new-project integration-services-lab --display-name="Lab Servicios"
```

> Ubicarse dentro del proyecto creado
```
oc project integration-services-lab
```

> Crear el config map con la parametrización
```
oc create -f despliegue/configmap-consulta-saldo-gateway.yml -n integration-services-lab
```

> Crear el build indicando que se va a subir un binario y la imagen base
```
oc new-build --binary=true --name=consulta-saldo-gateway openshift/java:8 -n integration-services-lab
```

> Se crea la aplicación
```
oc new-app integration-services-lab/consulta-saldo-gateway:latest --name=consulta-saldo-gateway --allow-missing-imagestream-tags=true -n integration-services-lab
```

> Se eliminan los triggers para que no haga deploy por cada cambio
```
oc set triggers dc/consulta-saldo-gateway --remove-all -n integration-services-lab
```

> Se crea el service por el puerto 8080
```
oc expose dc consulta-saldo-gateway --port 8080 -n integration-services-lab
```

> Se inicia el build enviando el jar generado
```
oc start-build consulta-saldo-gateway --from-file=target/consulta-saldo-gateway-1.0.0.jar --wait=true -n integration-services-lab
```

> Se realiza el deploy
```
oc rollout latest dc/consulta-saldo-gateway -n integration-services-lab
```

> Se exponse la ruta
```
oc expose svc consulta-saldo-gateway -n integration-services-lab
```

> Se obtiene la ruta generada para consumir el servicio
```
echo -en "\n\nhttp://$(oc get route consulta-saldo-gateway -o template --template={{.spec.host}} -n integration-services-lab)\n\n"
```

> Se consume el servicio con CURL
```
curl --location --request POST '{{RUTA_PASO_ANTERIOR}}/api/consultas/consultar-saldo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "numeroCuenta": "52122344255"
}'
```

## Author

* **Lázaro Miguel Coronado Torres** - *Middleware Senior Consultant - lcoronad@redhat.com* 
