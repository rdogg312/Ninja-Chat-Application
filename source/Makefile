main:
	javac -d . ./Graphic/*.java
	javac -cp .:assets/packages/* -d . ./Server/*.java
	javac -cp .:assets/packages/*:Graphic/*.java -d . ./Client/*.java
	javac -cp .:assets/packages/* ./*.java

client:
	java -cp .:assets/packages/* Ninja client

server:
	java -cp .:assets/packages/* Ninja server

clean:
	rm -rf ./*.class
	rm -rf ./Graphic/*.class
	rm -rf ./Server/*.class
