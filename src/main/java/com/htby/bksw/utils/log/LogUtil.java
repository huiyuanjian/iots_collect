package com.htby.bksw.utils.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LogUtil {

    public List<Map<String,List>> getChild( File file, List<Map<String,List>> list){
        File[] tempList = file.listFiles();
        if(tempList == null) return null;
        for (int i = 0; i < tempList.length; i++) {
            Map<String,List> map = new HashMap<>();
            if (tempList[i].isFile()) {
                map.put(tempList[i].getName(),null);
                list.add(map);
            }
            if (tempList[i].isDirectory()) {
                List<Map<String,List>> list_children = new ArrayList<>();
                list_children = getChild(tempList[i],list_children);
                map.put(tempList[i].getName(),list_children);
                list.add(map);
            }
        }
        return list;
    }
}
