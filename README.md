# Simple Server Monitor
It lets you monitor your minecraft server, with a GUI.
*(its free btw)*

## Setup
1. Install [the mod](https://github.com/redwoodsteve/simpleServerMonitor/releases)
2. Run the server
3. You should see a `simpleservermonitor` folder pop up
4. Inside the folder, you will find a `config.yml` file
5. Edit the config.yml file to your liking

## Connecting
1. Open a browser on your network
2. Type in your server's local ip, followed by ":8080" (example: 192.168.0.0:8080)
3. Connected!

### Connecting from outside your local network
Just port forward whatever port the monitor is on (default is 8080) and view from a browser

## Config
serverName: the name of the server. set to anything lol\
serverPort: the port that the monitor will be hosted on.
serverAuth: doesnt do anything right now, will be implemented "later"

## The GUI
![the gui](https://i.imgur.com/WlriOVg.png)
**Server name**: (top-left) the name of the server you are viewing\
**Log**: (bottom-center) the log of the server\
**Auto-scroll** (top-left of the log) a toggle button to enable/disable autoscroll on the log
