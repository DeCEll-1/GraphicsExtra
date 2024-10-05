package DeCell.GraphicsExtra.Helpers.classes;

import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;

public class fae_DamageBaseClass {

    public fae_DamageBaseClass(float damage, float empDamage, DamageType damageType) {
        this.Damage = damage;
        this.EMPDamage = empDamage;
        this.DamageType = damageType;
    }

    public fae_DamageBaseClass(float damage, float empDamage, DamageType damageType, DamagingExplosionSpec damagingExplosionSpec) {
        this.Damage = damage;
        this.EMPDamage = empDamage;
        this.DamageType = damageType;
        this.DamagingExplosionSpec = damagingExplosionSpec;
    }

    public fae_DamageBaseClass() {
    }

    public float Damage;
    public float EMPDamage;
    public DamageType DamageType;

    public DamagingExplosionSpec DamagingExplosionSpec;


}
