# Sprint 2 Troubleshooting Guide

## 1. Maven Cannot Find the Project

Error:

```text
There is no POM in this directory
```

Cause:

Maven was executed from the repository root instead of `Backend`.

Solution:

```powershell
cd Backend
mvn clean test
```

Alternatively:

```powershell
mvn -f .\Backend\pom.xml clean test
```

## 2. PostgreSQL Password Authentication Failed

Error:

```text
FATAL: password authentication failed for user "postgres"
```

Load the password from `.env`:

```powershell
$dbPasswordLine = Get-Content ..\.env |
    Where-Object { $_ -like "DB_PASSWORD=*" } |
    Select-Object -First 1

$env:DB_PASSWORD =
    $dbPasswordLine.Substring("DB_PASSWORD=".Length)

$env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
```

Ensure the configured password matches the PostgreSQL container password.

## 3. Datasource Driver Cannot Be Determined

Error:

```text
Failed to determine a suitable driver class
```

Configure the datasource:

```powershell
$env:SPRING_DATASOURCE_URL =
    "jdbc:postgresql://localhost:5433/membernet_test"

$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = $env:DB_PASSWORD
```

Then run:

```powershell
mvn clean test
```

## 4. Port 8080 Is Already in Use

Error:

```text
Web server failed to start. Port 8080 was already in use.
```

Check the process:

```powershell
Get-NetTCPConnection `
  -LocalPort 8080 `
  -State Listen |
Select-Object LocalAddress, LocalPort, OwningProcess
```

For temporary testing, use another port:

```powershell
mvn spring-boot:run `
  "-Dspring-boot.run.arguments=--server.port=8083"
```

## 5. Protected Endpoint Returns Data Without Login

Check that these files exist:

```text
SessionAuthenticationInterceptor.java
WebConfig.java
```

Check the property:

```properties
app.security.session-protection.enabled=${SESSION_PROTECTION_ENABLED:true}
```

Start with:

```powershell
$env:SESSION_PROTECTION_ENABLED = "true"
mvn spring-boot:run
```

## 6. BCrypt Warning

Warning:

```text
Encoded password does not look like BCrypt
```

Cause:

The database contains an invalid value in `password_hash`.

The account must be recreated through the application so that the configured `PasswordEncoder` generates a valid hash.

Do not store a plain-text password in `password_hash`.

## 7. Java Public Class Filename Error

Error:

```text
class X is public, should be declared in a file named X.java
```

Solution:

Ensure each public Java class, interface, enum or record has the same name as its file.

Also check for duplicate types in other files.

## 8. Frontend `.join()` Error

Error:

```text
Cannot read properties of undefined (reading 'join')
```

Cause:

The frontend expected a field that was not present in the login response.

The frontend must use the actual Sprint 2 response fields:

```text
userAccountId
loginEmail
displayName
accountStatus
homePage
```

## 9. Kubernetes Has No Current Context

Error:

```text
current-context is not set
```

Enable Kubernetes in Docker Desktop and verify:

```powershell
kubectl config get-contexts
kubectl config current-context
kubectl cluster-info
```

Expected context:

```text
docker-desktop
```

## 10. Kubernetes Image Cannot Be Pulled

Error:

```text
ErrImageNeverPull
```

Use an image available from a registry:

```yaml
image: arxhenta/membernet-sprint2:latest
imagePullPolicy: Always
```

Build and push:

```powershell
docker build `
  -t arxhenta/membernet-sprint2:latest `
  .\Backend

docker push arxhenta/membernet-sprint2:latest
```

## 11. Kubernetes YAML Parsing Error

Error:

```text
did not find expected key
```

Check line numbers:

```powershell
$lineNumber = 0

Get-Content .\k8s\sprint2\membernet-sprint2.yaml |
ForEach-Object {
    $lineNumber++
    "{0,4}: {1}" -f $lineNumber, $_
}
```

Validate after correcting indentation:

```powershell
kubectl apply --dry-run=client `
  -f .\k8s\sprint2\membernet-sprint2.yaml
```

## 12. Kubernetes Unknown Fields

Example:

```text
unknown field "spec.template.metadata.containers"
```

Cause:

`containers` or `volumes` were incorrectly placed under `metadata`.

Correct structure:

```yaml
template:
  metadata:
    labels:
      app: membernet
  spec:
    containers:
      - name: membernet
```

## 13. Kubernetes Secret Update

Update the secret from `.env`:

```powershell
kubectl create secret generic membernet-secrets `
  --namespace membernet `
  --from-env-file=.\.env `
  --dry-run=client `
  -o yaml |
kubectl apply -f -
```

Restart the application:

```powershell
kubectl rollout restart `
  deployment/membernet `
  -n membernet
```

## 14. Pod Diagnostics

Check pods:

```powershell
kubectl get pods -n membernet
```

Describe a pod:

```powershell
kubectl describe pod `
  -n membernet `
  POD_NAME
```

Check logs:

```powershell
kubectl logs `
  -n membernet `
  deployment/membernet `
  --tail=100
```

## 15. Port Forward Stops Working

The port-forward command must remain active:

```powershell
kubectl port-forward `
  -n membernet `
  service/membernet `
  8081:8080
```

Closing that terminal stops access through port `8081`.
