package com.example.ZPI.Model;

import com.example.ZPI.entities.Match;
import com.example.ZPI.entities.Performance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@Getter
@Setter
public class PreparedMatch {
    private Match match;
    private HashMap<String, Performance> performances;

}
