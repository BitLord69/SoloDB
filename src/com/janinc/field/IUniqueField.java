package com.janinc.field;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-17
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import com.janinc.DataObject;

public interface IUniqueField {
   boolean isUnique();
   void updateDirtyField(DataObject d);
}
