package io.example.test;

public class LectureTheatre extends Building {
    public LectureTheatre(Vector2i pos) {
        super(pos, Consts.LECTURE_THEATRE_CAPACITY_L1, BuildingType.LectureTheatre);
        type = BuildingType.LectureTheatre;
    }


    @Override
    public void upgrade() {
        
    }

    @Override
    public void downgrade() {

    }

}
