package course.labs.classroomquery.Model;


public class ClassroomBean {
    private String classroom;
    //教室是否有暖气、电源、空调
    private boolean hasHeat;
    private boolean hasElectricity;
    private boolean hasAC;

    //是否离卫生间、饮水机近
    private boolean nearToliet;
    private boolean nearWater;

    //是否在北洋园校区
    private boolean inPeiyangyuan;

    //是否被占用、即将被占用
  //  private boolean occupied;
 //   private boolean willBeOccupied;
    private int building;
    private String state;
    //是否已收藏
    private boolean collected;

   public ClassroomBean(FreeRoom2.FreeRoom freeRoom1){
        this.classroom = freeRoom1.getRoom();
        this.state = freeRoom1.getState();
        this.hasHeat = freeRoom1.getHeating();
        this.collected = freeRoom1.isCollected();
        this.nearToliet = false;
        this.hasAC = false;
        this.nearWater = freeRoom1.getWater_dispenser();
        this.hasElectricity = freeRoom1.getPower_pack();
    }
    public ClassroomBean(CollectedRoom2.CollectedRoom CollectedRoom){
        this.classroom = CollectedRoom.getClassroom();
        this.collected = true;
        this.state = CollectedRoom.getState();
        this.hasHeat = CollectedRoom.getHeating();
        this.nearToliet = false;
        this.nearWater = CollectedRoom.getWater();
        this.hasElectricity = CollectedRoom.getPower();
    }
   public ClassroomBean(boolean hasHeat, boolean hasElectricity, boolean hasAC,
                        boolean nearToliet, boolean nearWater,
                        boolean inPeiyangyuan,
                        boolean occupied, boolean willBeOccupied, boolean collected, int building, String classroom) {

        this.hasHeat = hasHeat;
        this.hasElectricity = hasElectricity;
        this.hasAC = hasAC;

        this.nearToliet = nearToliet;
        this.nearWater = nearWater;

        this.inPeiyangyuan = inPeiyangyuan;

        //this.occupied = occupied;
        //this.willBeOccupied = willBeOccupied;
        this.collected = collected;
        this.building = building;
        this.classroom = classroom;

    }

    public boolean isHasHeat() {
        return hasHeat;
    }

    public void setHasHeat(boolean hasHeat) {
        this.hasHeat = hasHeat;
    }

    public boolean isHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public boolean isHasAC() {
        return hasAC;
    }

    public void setHasAC(boolean hasAC) {
        this.hasAC = hasAC;
    }

    public boolean isNearToliet() {
        return nearToliet;
    }

    public void setNearToliet(boolean nearToliet) {
        this.nearToliet = nearToliet;
    }

    public boolean isNearWater() {
        return nearWater;
    }

    public void setNearWater(boolean nearWater) {
        this.nearWater = nearWater;
    }

    public boolean isInPeiyangyuan() {
        return inPeiyangyuan;
    }

    public void setInPeiyangyuan(boolean inPeiyangyuan) {
        this.inPeiyangyuan = inPeiyangyuan;
    }

    /*public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isWillBeOccupied() {
        return willBeOccupied;
    }
    */
    public String getState(){
        return  state;
    }

   /* public void setWillBeOccupied(boolean willBeOccupied) {
        this.willBeOccupied = willBeOccupied;
    }*/
    public int getBuilding(){
        return this.building;
    }
    public void setBuilding(int building1){
        this.building = building1;
    }
    public boolean isCollected(){
        return  this.collected;
    }
    public void setCollected(boolean collected){
        this.collected = collected;
    }
    public  String getRoomNumber(){
        return  this.classroom;
    }
    public int getNumber(){
        return Integer.valueOf(classroom.substring(0,2));
    }
    public  void setRoomNumber(String roomNumber1) {
        this.classroom = roomNumber1;
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof ClassroomBean){
            if(this.getRoomNumber().equals(((ClassroomBean) o).getRoomNumber())){
                System.out.println("集合发挥了作用");
                return true;
            }
        }
        return false;
    }
}
