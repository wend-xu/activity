package com.learn.activitisample3.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;


import java.io.IOException;

public class ConvertTool {

    //model 源數據轉換爲 bpmnModel
    public static BpmnModel modelEditorSourceToBpmnModel(byte[] modelEditorSource){
        BpmnModel bpmnModel = null;
        try {
            JsonNode node = new ObjectMapper().readTree(modelEditorSource);
            BpmnJsonConverter converter = new BpmnJsonConverter();
            bpmnModel = converter.convertToBpmnModel(node);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bpmnModel;
    }
}
