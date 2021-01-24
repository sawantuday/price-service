package org.upstox.tickerService.service;

import com.google.gson.Gson;
import org.upstox.tickerService.model.Tick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class DataService {

    public void getTradeData(){
        try {
            Gson gson = new Gson();
            String line;

            URL url = getClass().getClassLoader().getResource("trades.json");
            File file = new File(url.getPath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int limit = 0;
            while((line = bufferedReader.readLine()) != null){
                Tick model = gson.fromJson(line, Tick.class);
                System.out.println(model);
                if(limit++ > 4){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        DataService dataService = new DataService();
        dataService.getTradeData();
    }

}
