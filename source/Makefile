main:
	javac -d . ./Graphic/*.java
	javac -d . ./Server/*.java
	javac -cp .:assets/packages/* ./*.java

client:
	java -cp .:assets/packages/* Ninja client

server:
	java -cp .:assets/packages/* Ninja server

clean:
	rm -rf ./*.class
	rm ./Graphic/*.class
