package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem {

    //Initial states
    int[] initial_left_state;
    int[] initial_right_state;
    //Final state we should be in
    int[] goal_left_state;
    int[] goal_right_state;

    //TTime for each person
    int[] times;
    //Maximum time for the problem
    int max_time;
    //Torch left - true : Torch right - false
    boolean torch ;
    int heuristic;


    public Problem(int n, int[] times, int max_time, int h){

        this.times = times;
        this.max_time = max_time;
        this.torch = true;
        this.heuristic = h;
        this.initial_left_state = new int[n];
        this.initial_right_state = new int[n];
        this.goal_left_state = new int[n];
        this.goal_right_state = new int[n];

        for(int i =0;i<n; i++){
            this.initial_left_state[i]=1;
            this.initial_right_state[i]=0;
            this.goal_left_state[i]=0;
            this.goal_right_state[i]=1;
        }
    }



    public List<State> go_right(State state){

        this.torch = false;
        ArrayList<State> new_states = new ArrayList<State>();
        int size = initial_left_state.length;
        for(int i=0; i<size; i++){
            for(int j=i+1;j<size;j++ ){
                if(state.current_left_state[i]==1 && state.current_left_state[j]==1){
                    int[] new_current_left = state.current_left_state.clone();
                    int[] new_current_right = state.current_right_state.clone();
                    new_current_left[i]=0;
                    new_current_left[j]=0;
                    new_current_right[i]=1;
                    new_current_right[j]=1;
                    State new_state;
                    if(this.heuristic==0 || this.heuristic==1){
                        new_state = new State(new_current_left, new_current_right, state,state.path_cost + Math.max(times[i],times[j]), this.heuristic);
                        //new_state.path_cost = state.path_cost + Math.max(times[i],times[j]);
                        new_states.add(new_state);
                    }else{
                        new_state = new State(new_current_left, new_current_right, state,state.path_cost + Math.max(times[i],times[j]));
                        new_state.path_cost = state.path_cost + Math.max(times[i],times[j]);
                        new_states.add(new_state);
                    }

                }
            }
        }
        return new_states;
    }

    public List<State> go_left(State state){

        this.torch = true;
        ArrayList<State> new_states = new ArrayList<>();
        int index = min_time(state.current_right_state);
        int[] new_current_left = state.current_left_state.clone();
        int[] new_current_right = state.current_right_state.clone();
        new_current_left[index] = 1;
        new_current_right[index]=0;
        State new_state;
        if(this.heuristic==0 || this.heuristic==1){
            new_state = new State(new_current_left, new_current_right, state,state.path_cost + this.times[index], this.heuristic);
            //new_state.path_cost = state.path_cost + this.times[index];
            new_states.add(new_state);
        }else{
            new_state = new State(new_current_left, new_current_right, state,state.path_cost + this.times[index]);
            //new_state.path_cost = state.path_cost + this.times[index];
            new_states.add(new_state);
        }
        return new_states;
    }


    int min_time( int[] right_state){
        int index =-1;
        int mini = this.max_time;
        for(int i =0;i<this.times.length;i++){
            if(right_state[i]==1){
                if(this.times[i]<mini){
                    mini = this.times[i];
                    index =i;
                }
            }
        }
        return index;
    }

    boolean goal_test(State s){
        if(Arrays.equals(s.current_left_state, this.goal_left_state) && Arrays.equals(s.current_right_state, this.goal_right_state)){
            return true;
        }else{
            return false;
        }
    }

}
