package com.Regestration.security.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Verification {
    private  static final int ExpireTime=10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expireTime;
        @OneToOne
    @JoinColumn(name = "user_id",
    nullable = false,foreignKey = @ForeignKey(name = "FK_VERIFICATION_TOKEN"))
    private User user;

        public Verification(User user,String token){
            super();
            this.user=user;
            this.token=token;
            this.expireTime= calculateExpirationTime(ExpireTime);
        }
        public Verification(String token){
            super();
            this.token=token;
            this.expireTime= calculateExpirationTime(ExpireTime);
        }

    private Date calculateExpirationTime(int expireTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,expireTime);
        return new Date(calendar.getTime().getTime());
    }

}
