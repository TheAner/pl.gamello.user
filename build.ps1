# Author: Adrian Sowiński TheAner
#
# Description: Script build docker image, by given variables,
# removes previous images with same version and pushes image to AWS ECR.
#
# Usage: Open PowerShell in main folder and run ".\build"
#-------------------------------------------------------------
$appName = "user"
$appVersion = "0.4.1"
$appService = "gamello"
$ecrUrl = ""
#-------------------------------------------------------------
$ErrorActionPreference = "Stop"

function invoke-maven {
    param(
        $command,
        $RELATIVE_PATH
    )
    Write-Host "`n***" -f Green
    Write-Host "***" -f Green -NoNewline
    Write-Host "Running: $command" -f Yellow
    Write-Host "***" -f Green
    $LOCATION = Get-Location
    Set-Location "$LOCATION$RELATIVE_PATH"
    $SUCCESS = $false
    Invoke-Expression "$command" | foreach {
        Write-Host $_
        if($_.Contains("BUILD SUCCESS")){
            $SUCCESS = $true
        }
    }
    Set-Location "$LOCATION"
    if( -not $SUCCESS){
        Write-Host "`n***" -f Green
        Write-Host "***" -f Green -NoNewline
        Write-Host "Build failure for: '$command'"-f Yellow
        Write-Host "***" -f Green
        exit
    }
}

Write-Host "***" -f Green
Write-Host "***" -f Green -NoNewline
Write-Host "Repackaging jars" -f Yellow
Write-Host "***" -f Green
invoke-maven "mvn clean package -DskipTests"

Write-Host "`n***" -f Green
Write-Host "***" -f Green -NoNewline
Write-Host "Building image" -f Yellow
Write-Host "***" -f Green
docker build --build-arg "JAR_FILE=target/$($appName)-$($appVersion).jar" -t "$($appService)/$($appName):$($appVersion)" .

Write-Host "`n***" -f Green
Write-Host "***" -f Green -NoNewline
Write-Host "Tagging Docker image" -f Yellow
Write-Host "***" -f Green
docker tag "$($appService)/$($appName):$($appVersion)" "$($ecrUrl)/$($appService)/$($appName):$($appVersion)"

Write-Host "`n***" -f Green
Write-Host "***" -f Green -NoNewline
Write-Host "Docker login to AWS ECR" -f Yellow
Write-Host "***" -f Green
invoke-expression $(aws2 ecr get-login --no-include-email --region eu-central-1)

Write-Host "`n***" -f Green
Write-Host "***" -f Green -NoNewline
Write-Host "Removing old images with same version" -f Yellow
Write-Host "***" -f Green
$ecrResponse = $(aws2 ecr batch-delete-image --repository-name "$($appService)/$($appName)" --image-ids imageTag="$($appVersion)")
Write-Output $ecrResponse

Write-Host "`n***" -f Green
Write-Host "***" -f Green -NoNewline
Write-Host "Pushing image to AWS ECR" -ForegroundColor Yellow
Write-Host "***" -f Green
docker push "$($ecrUrl)/$($appService)/$($appName):$($appVersion)"