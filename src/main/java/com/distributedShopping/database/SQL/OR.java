package com.distributedShopping.database.SQL;

public class OR implements Condition {

    private Object[] conditionList;

    public OR(Object ...a){

        this.conditionList = a;
    }
    
    @Override
    public String toString() {
        StringBuilder sbCondition = new StringBuilder();

        String className =  " " + this.getClass().getSimpleName() + " ";

        for(int i=0; i<conditionList.length;i++){
            if(i==0){
                sbCondition.append(conditionList[i]);
            } else {
                sbCondition.append(className + conditionList[i]);
            }
        }

        String condition = "(" + sbCondition.toString() + ")";

        return condition;
    }
}