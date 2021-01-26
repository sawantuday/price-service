# Ticker service
## How to run 
1. Download trade data and copy trades.json under src/main/resources directory
2. Compile and package code with `mvn clean package`
3. Start server with `java -jar target/tickerService-1.0-SNAPSHOT.jar`  

## WebSocket client example 
Run this code in Chrome or any other browsers console which supports WebSockets 
This snippet will initiate WebSocket client connection and will 
subscribe for multiple symbols to receive updates
  
```
const ws = new WebSocket('ws://localhost:8080/v1/ticker');
ws.onopen = function(){console.log('WebSocket connection is open')}
ws.onerror = function(err){console.log('Error on WebSocket connection'); console.log(err);}
ws.onmessage = function(e){console.log(e.data);}
ws.onclose = function(){console.log('WebSocket connection is closed');}

const symbols = ['XXBTZUSD', 'XETHZUSD', 'XXMRXXBT', 'XXRPXXBT', 'ADAEUR'];
for(let i in symbols){
     const msg = {message:'subscribe', symbol:symbols[i]};
     ws.send(JSON.stringify(msg));
}
```

### Docker container ?
This service can also be deployed as Docker container, root directory contains Dockerfile with all required configuration 
1. Install docker 
2. Run docker build from root directory to create image  
   `docker build -t ticker-service `
3. Once image is ready it can be started with 
   `docker run -it -p:8080:8080 ticker-service`  