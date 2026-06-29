package com.system.paperflow.domain.state;
import com.system.paperflow.domain.entity.Paper;


public class SubmittedState implements PaperState {

    @Override
    public String getStatus(){
        return "Submitted";
    }


    @Override
    public void advance(Paper paper){
        paper.setState(new UnderReviewState());
    }


}
