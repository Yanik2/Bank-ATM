public class Request {
    public String id;
    public String option;
    public String amount;
    public String destinationId;

    public Request(String request) {
        String[] reqArr = request.split("#");
        this.id = reqArr[0];
        this.option = reqArr[1];
        this.amount = reqArr[2];
        this.destinationId = reqArr[3];
    }

}
