package xin.lz1998.zbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 新人入群欢迎内容
 */
@Entity
@Table(name = "zbot_welcome")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Welcome {
    /**
     * 群号
     */
    @Id
    private Long groupId;

    /**
     * 欢迎内容
     */
    @Column(length = 2000)
    private String welcomeMsg;

    /**
     * 设置者QQ
     */
    @Column(nullable = false)
    private Long adminId;

    /**
     * 修改时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private Date gmtModified;
}
