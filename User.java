package es.tid.wallet.model.users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.tid.wallet.model.cards.Card;
import es.tid.wallet.model.coupons.Coupon;
import es.tid.wallet.model.payments.PaymentMethod;
import es.tid.wallet.model.payments.PaymentMethod.PaymentMethodType;

/**
 * User Entity.
 * Useless line
 * Modified again.
 * Line to keep.
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    /**
     * User Status.
     */
    public static enum UserStatus {
        UNREGISTERED, REGISTERED, VALIDATE_PENDING, BLOCKED, LOGIN, PIN_BLOCKED
    };

    /**
     * Pin Status.
     */
    public enum PinStatus {
        OFF, ACTIVATED, DEACTIVATED
    };

    private static final long serialVersionUID = 2548439201124643740L;
    public static final int MAX_ATTEMPTS = 3;

    @Id
    @Column(name = "msisdn", nullable = false)
    private String msisdn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "pin", nullable = true)
    private String pin;

    @Enumerated(EnumType.STRING)
    @Column(name = "pinStatus", nullable = true)
    private PinStatus pinStatus = PinStatus.OFF;

    @Column(name = "userId", nullable = true)
    private String userId;

    @Column(name = "pinAttempts", nullable = false)
    private int pinAttempts = 0;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "authToken", nullable = true)
    private String authToken;

    @Column(name = "authPass", nullable = true)
    private String authPass;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Column(name = "last_access_date", nullable = false)
    private Date lastAccessDate;

    @OneToMany(targetEntity = Card.class, cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Card> cards;

    @Transient
    private List<PaymentMethod> paymentMethods;

    @Transient
    private List<Coupon> coupons;
    
    @Transient
    private int availableCouponResults;
    
    @Transient
    private List<Message> messages;
    
    @Transient
    private boolean hasAccount;

    @Transient
    private boolean hasPayments;

    @Transient
    private boolean hasWallet;

    @Transient
    private boolean peopleIsAttending;

    /**
     * Constructor.
     */
    public User() {
        cards = new ArrayList<Card>();
        messages = new ArrayList<Message>();
        creationDate = new Date();
        lastAccessDate = new Date();
    }

    public User(String msisdn, String userId, UserStatus status) {
        this();
        this.msisdn = msisdn;
        this.userId = userId;
        this.status = status;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public PinStatus getPinStatus() {
        if (pinStatus == null) {
            pinStatus = PinStatus.OFF;
        }
        return pinStatus;
    }

    public void setPinStatus(PinStatus pinStatus) {
        this.pinStatus = pinStatus;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getPinAttempts() {
        return pinAttempts;
    }

    public void setPinAttempts(int pinAttempts) {
        this.pinAttempts = pinAttempts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public int getAvailableCouponResults() {
        return availableCouponResults;
    }

    public void setAvailableCouponResults(int availableCouponResults) {
        this.availableCouponResults = availableCouponResults;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthPass() {
        return authPass;
    }

    public void setAuthPass(String authPass) {
        this.authPass = authPass;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }

    public boolean isHasPayments() {
        return hasPayments;
    }

    public void setHasPayments(boolean hasPayments) {
        this.hasPayments = hasPayments;
    }

    /**
     * Checks if user has SVA.
     * 
     * @return
     */
    public boolean hasSVA() {
        boolean hasSVA = false;

        if (paymentMethods != null) {
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentMethod.getType() == PaymentMethod.PaymentMethodType.EMONEY) {
           // Let's write a useless comment
                    hasSVA = true;
                    break;
                }
            }
        }
        return hasSVA;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [msisdn=").append(msisdn).append(", status=").append(status).append(", pin=").append(pin)
                .append(", pinStatus=").append(pinStatus).append(", userId=").append(userId).append(", pinAttempts=")
                .append(pinAttempts).append(", country=").append(country).append(", authToken=").append(authToken)
                .append(", authPass=").append(authPass).append(", creationDate=").append(creationDate)
                .append(", lastAccessDate=").append(lastAccessDate).append(", cards=").append(cards)
                .append(", paymentMethods=").append(paymentMethods).append(", couponInstanceNews=").append(coupons)
                .append(", availableCouponResults=").append(availableCouponResults).append(", hasAccount=")
                .append(hasAccount).append(", hasPayments=").append(hasPayments).append("]");
        return builder.toString();
    }

    public boolean isHasWallet() {
        return hasWallet;
    }

    public void setHasWallet(boolean hasWallet) {
        this.hasWallet = hasWallet;
    }
}
