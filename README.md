# DOSChat
A real-time chat application in Terminal using Sockets


### Usage
- compile Server & Client
  ```bash
  javac Server.java
  javac Client.java
  ```
- Run the Server first & keep it Running
  ```bash
  java Server
  ```
- Connecting to the Server
  - Run the Client 
    ```bash
    java Client
    ```
  - Enter the IP (if running in a Server otherwise leave it blank)
    ```
    Enter the IP Add.(blank if local) : 123.456.789.132
    ```
  - Enter the Port(can change this in Server.java)
    ```
    Enter the PORT : 9001
    ```
  - After entering a username You can start chatting
  - use ```..help``` to see more commands
  - exit using ```..exit```