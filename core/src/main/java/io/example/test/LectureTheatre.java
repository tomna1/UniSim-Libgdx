package io.example.test;

import com.badlogic.gdx.Gdx;

public class LectureTheatre extends Building {
    public LectureTheatre(Vector2i pos) {
        super(pos, Consts.LECTURE_THEATRE_CAPACITY_L1, BuildingType.LectureTheatre);
    }


    @Override
    public void upgrade() {
        if (level < 1 || level >= 3) return;
        
        short newLevel = (short)(level + 1);
        if (newLevel == 2) {
            setCapacity((short)Consts.LECTURE_THEATRE_CAPACITY_L2);
        } else if (newLevel == 3) {
            setCapacity((short)Consts.LECTURE_THEATRE_CAPACITY_L3);
        }
        
        level = newLevel;
        if (Consts.BUILDING_LEVEL_CHANGE_MODE_ON) {
            Gdx.app.log("LectureTheatre", type + " at point " + pos + " has been upgraded to level " + newLevel);
        }
    }

    @Override
    public void downgrade() {
        if (level <= 1 || level > 3) return;

        short newLevel = (short)(level - 1);
        if (newLevel == 1) {
            setCapacity((short)Consts.LECTURE_THEATRE_CAPACITY_L1);
        } else if (newLevel == 2) {
            setCapacity((short)Consts.LECTURE_THEATRE_CAPACITY_L2);
        }

        level = newLevel;
        if (Consts.BUILDING_LEVEL_CHANGE_MODE_ON) {
            Gdx.app.log("LectureTheatre", type + " at point " + pos + " has been downgraded to level " + newLevel);
        }
    }

}
