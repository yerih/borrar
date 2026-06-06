@echo off
cd /d C:\repositorios\cc3
call gradlew.bat installDebug -x lint
pause
