# Red Hat Integration Lab

Repositorio con una una ruta de integración camel de ejemplo para desplegar en Openshift Container Platform versión 3.11 con S2I.

## Descripción de funcionalidad

Esta ruta de integración expone un servicio Rest que sirve como Gateway hacia otro microservicio en SpringBoot, el request recibe un número de cuenta y devuelve el saldo de la cuenta.

## URL

Reemplazar {HOST_OCP} por el host de ruta generada en el ambiente de OCP desplegado
```
http://{HOST_OCP}/api/consultas/consultar-saldo
```

## Request

```
{
    "numeroCuenta": "12222"
}
```

## Response

```
{
    "saldo": 2300000,
    "fecha": "2020-11-18T20:12:57.079+00:00"
}
```

## Pasos de instalación de OCP

A continuación se describen los pasos para realizar el despliegue de esta ruta de integración en un ambiente de OCP 3.11 o 4.x, una vez se haya clonado este repositorio

cd redhat-integration-lab

mvn clean package

oc login --server=https://{{HOST_CONSOLE_OCP}}}:{{PORT_CONSOLE_OCP}} --username={{USER_OCP}} --password={{PASSWORD_OCP}} --insecure-skip-tls-verify=true

oc new-project integration-services-lab --display-name="Lab Servicios"

oc project integration-services-lab

oc create -f despliegue/configmap-consulta-saldo-gateway.yml -n integration-services-lab

oc new-build --binary=true --name=consulta-saldo-gateway openshift/java:8 -n integration-services-lab

oc new-app integration-services-lab/consulta-saldo-gateway:latest --name=consulta-saldo-gateway --allow-missing-imagestream-tags=true -n integration-services-lab

oc set triggers dc/consulta-saldo-gateway --remove-all -n integration-services-lab

oc expose dc consulta-saldo-gateway --port 8080 -n integration-services-lab

oc start-build consulta-saldo-gateway --from-file=target/consulta-saldo-gateway-1.0.0.jar --wait=true -n integration-services-lab

oc rollout latest dc/consulta-saldo-gateway -n integration-services-lab

oc expose svc consulta-saldo-gateway -n integration-services-lab

echo -en "\n\nhttp://$(oc get route consulta-saldo-gateway -o template --template={{.spec.host}} -n integration-services-lab)\n\n"

curl --location --request POST '{{RUTA_PASO_ANTERIOR}}/api/consultas/consultar-saldo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "numeroCuenta": "52122344255"
}'

## Author

* **Lázaro Miguel Coronado Torres** - *Middleware Senior Consultant - lcoronad@redhat.com* 
