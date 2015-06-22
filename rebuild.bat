call "%~dp0gradlew" clean jar test
if %errorlevel% neq 0 pause
