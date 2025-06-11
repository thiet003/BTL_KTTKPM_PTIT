# Deploy script for Kubernetes
Write-Host "Deploying F1 Management System to Kubernetes..." -ForegroundColor Green

# Apply manifests in order
$manifests = @(
    "00-namespace.yaml",
    "01-configmap.yaml", 
    "02-persistent-volumes.yaml",
    "03-eureka-server.yaml",
    "04-rabbitmq.yaml",
    "05-databases.yaml",
    "06-race-schedule-service.yaml",
    "07-participant-service.yaml", 
    "08-race-result-service.yaml",
    "09-standings-service.yaml",
    "10-api-gateway.yaml"
)

foreach ($manifest in $manifests) {
    Write-Host "Applying $manifest..." -ForegroundColor Cyan
    kubectl apply -f $manifest
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Successfully applied $manifest" -ForegroundColor Green
    } else {
        Write-Host "❌ Failed to apply $manifest" -ForegroundColor Red
        exit 1
    }
    Start-Sleep -Seconds 2
}

Write-Host "`n🎉 Deployment completed!" -ForegroundColor Green
Write-Host "Checking deployment status..." -ForegroundColor Yellow

# Check pods status
kubectl get pods -n f1-app

Write-Host "`nTo access the application:" -ForegroundColor Yellow
Write-Host "API Gateway: http://$(minikube ip):30080" -ForegroundColor Cyan
Write-Host "Eureka Server: kubectl port-forward -n f1-app svc/eureka-server 8761:8761" -ForegroundColor Cyan
Write-Host "RabbitMQ Management: kubectl port-forward -n f1-app svc/rabbitmq 15672:15672" -ForegroundColor Cyan

Write-Host "`nUseful commands:" -ForegroundColor Yellow
Write-Host "- Watch pods: kubectl get pods -n f1-app -w" -ForegroundColor White
Write-Host "- View logs: kubectl logs -n f1-app <pod-name>" -ForegroundColor White
Write-Host "- Delete all: kubectl delete namespace f1-app" -ForegroundColor White 