javac -d . ./Graphic/*.java
javac -cp ;./assets/packages/* -d . ./Server/*.java
java -cp ;./assets/packages/* Ninja server