# Deploy script for LIGHTWEIGHT Kubernetes version
Write-Host "Deploying F1 Management System (LIGHTWEIGHT) to Kubernetes..." -ForegroundColor Green
Write-Host "Resource usage: ~1.2GB RAM total (instead of ~4GB)" -ForegroundColor Yellow

# Apply lightweight deployment
Write-Host "Applying lightweight deployment..." -ForegroundColor Cyan
kubectl apply -f lightweight-deployment.yaml

if ($LASTEXITCODE -eq 0) {
    Write-Host "Successfully deployed lightweight version" -ForegroundColor Green
} else {
    Write-Host "Failed to deploy" -ForegroundColor Red
    exit 1
}

Write-Host "`nLightweight deployment completed!" -ForegroundColor Green
Write-Host "Checking deployment status..." -ForegroundColor Yellow

# Wait a bit for pods to start
Start-Sleep -Seconds 5

# Check pods status
kubectl get pods -n f1-app-lite

Write-Host "`nResource summary:" -ForegroundColor Yellow
Write-Host "- Eureka Server: 128MB" -ForegroundColor White
Write-Host "- MySQL (shared): 512MB" -ForegroundColor White  
Write-Host "- RabbitMQ: 128MB" -ForegroundColor White
Write-Host "- Race Schedule Service: 128MB" -ForegroundColor White
Write-Host "- Participant Service: 128MB" -ForegroundColor White
Write-Host "- Race Result Service: 128MB" -ForegroundColor White
Write-Host "- Standings Service: 128MB" -ForegroundColor White
Write-Host "- API Gateway: 128MB" -ForegroundColor White
Write-Host "- Total: ~1.2GB (instead of 4GB+)" -ForegroundColor Green

Write-Host "`nTo access the application:" -ForegroundColor Yellow
Write-Host "API Gateway: http://$(minikube ip):30080" -ForegroundColor Cyan
Write-Host "Eureka Server: kubectl port-forward -n f1-app-lite svc/eureka-server 8761:8761" -ForegroundColor Cyan
Write-Host "RabbitMQ Management: kubectl port-forward -n f1-app-lite svc/rabbitmq 15672:15672" -ForegroundColor Cyan

Write-Host "`nUseful commands:" -ForegroundColor Yellow
Write-Host "- Watch pods: kubectl get pods -n f1-app-lite -w" -ForegroundColor White
Write-Host "- View logs: kubectl logs -n f1-app-lite <pod-name>" -ForegroundColor White
Write-Host "- Delete: kubectl delete namespace f1-app-lite" -ForegroundColor White 