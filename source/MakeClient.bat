javac -d . ./Graphic/*.java
javac -cp .;assets/packages/* -d . ./Server/*.java
javac -cp .;assets/packages/*;Graphic/*.java -d . ./Client/*.java
javac -cp .;assets/packages/* ./*.java
java -cp .;assets/packages/json-simple.jar Ninja client