package xin.lz1998.zbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 用于保存群主自定义的管理员，有机哥指令操作权限
 */
@Entity
@Table(name = "zbot_admin")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AdminKey.class)
public class Admin {
    /**
     * 群号
     */
    @Id
    @Column
    private Long groupId;

    /**
     * QQ
     */
    @Id
    @Column(nullable = false)
    private Long userId;

    /**
     * 是否管理员
     */
    @Column(nullable = false)
    private Boolean isAdmin;

    /**
     * 修改时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private Date gmtModified;
}
