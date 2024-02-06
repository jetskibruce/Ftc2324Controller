package org.firstinspires.ftc.teamcode.macros.auto;

import android.annotation.SuppressLint;

import java.util.Objects;

public class FindTeamElementPosition  {

    String WhereElement = null;

    //The camera working and sensing all goes here




     @SuppressLint("NotConstructor")
     public String FindTeamElementPosition() {

         if (Objects.equals(WhereElement, "left")) {
             return "left";
         }
         else if (Objects.equals(WhereElement, "right")) {
             return "right";
         }
         else {
             return "center";
         }
     }

}


