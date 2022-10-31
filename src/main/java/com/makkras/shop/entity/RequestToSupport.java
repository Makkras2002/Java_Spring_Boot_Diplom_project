package com.makkras.shop.entity;

import javax.persistence.*;

@Entity(name = "requests_to_support")
public class RequestToSupport extends CustomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_request_id")
    private Long requestId;

    @Column(name = "request_message")
    private String requestMessage;

    @Column(name = "response_message")
    private String responseMessage;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "requester_user_id")
    private User requester;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "responder_employee_id")
    private User responder;

    @Column(name = "is_completed")
    private boolean isCompleted;

    public RequestToSupport() {
    }

    public RequestToSupport(String requestMessage, String responseMessage, User requester, User responder, boolean isCompleted) {
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
        this.requester = requester;
        this.responder = responder;
        this.isCompleted = isCompleted;
    }

    public RequestToSupport(Long requestId, String requestMessage, String responseMessage, User requester, User responder, boolean isCompleted) {
        this.requestId = requestId;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
        this.requester = requester;
        this.responder = responder;
        this.isCompleted = isCompleted;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getResponder() {
        return responder;
    }

    public void setResponder(User responder) {
        this.responder = responder;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestToSupport that = (RequestToSupport) o;

        if (isCompleted != that.isCompleted) return false;
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) return false;
        if (requestMessage != null ? !requestMessage.equals(that.requestMessage) : that.requestMessage != null)
            return false;
        if (responseMessage != null ? !responseMessage.equals(that.responseMessage) : that.responseMessage != null)
            return false;
        if (requester != null ? !requester.equals(that.requester) : that.requester != null) return false;
        return responder != null ? responder.equals(that.responder) : that.responder == null;
    }

    @Override
    public int hashCode() {
        int result = requestId != null ? requestId.hashCode() : 0;
        result = 31 * result + (requestMessage != null ? requestMessage.hashCode() : 0);
        result = 31 * result + (responseMessage != null ? responseMessage.hashCode() : 0);
        result = 31 * result + (requester != null ? requester.hashCode() : 0);
        result = 31 * result + (responder != null ? responder.hashCode() : 0);
        result = 31 * result + (isCompleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestToSupport{");
        sb.append("requestId=").append(requestId);
        sb.append(", requestMessage='").append(requestMessage).append('\'');
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", requester=").append(requester);
        sb.append(", responder=").append(responder);
        sb.append(", isCompleted=").append(isCompleted);
        sb.append('}');
        return sb.toString();
    }
}
