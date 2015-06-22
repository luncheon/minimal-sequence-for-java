call "%~dp0gradlew" clean build javadoc
if %errorlevel% neq 0 pause
