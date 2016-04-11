main:
	javac -d . ./Encryption/*.java
	javac -d . ./Graphic/*.java
	javac -cp .:assets/packages/* -d . ./Server/*.java
	javac -cp .:assets/packages/*:Graphic/*.java:Encryption/*.java -d . ./Client/*.java
	javac -cp .:assets/packages/* ./*.java

client:
	java -cp .:assets/packages/* Ninja client

server:
	java -cp .:assets/packages/* Ninja server

clean:
	rm -rf ./*.class
	rm -rf ./Encryption/*.class
	rm -rf ./Graphic/*.class
	rm -rf ./Server/*.class
	rm -rf ./Client/*.class
