package com.example.soccerleague.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class PageViewModel extends ViewModel {
    private static int teamCount=0;

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    private static List<String> list_fixture=new ArrayList<>();
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            String half="First";
            if (input>teamCount/2){
                half="second";
            }
            return "WEEK"+input+"("+half+" Half)\n\n"+list_fixture.get(input-1);
        }
    });

    public List<String> getList_fixture() {
        return list_fixture;
    }

    public void setList_fixture(List<String> list_fixture) {
        this.list_fixture = list_fixture;
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}