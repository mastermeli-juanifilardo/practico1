package classes;

public enum ResponseStatus {

        SUCCESS ("Success"),
        ERROR ("Error");

        private String status;

        ResponseStatus(String status) {
            this.status = status;
        }
}
