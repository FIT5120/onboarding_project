    package com.example.onboarding_backend.mapper;

    import com.baomidou.mybatisplus.core.mapper.BaseMapper;
    import com.example.onboarding_backend.entity.Geo;
    import org.apache.ibatis.annotations.Mapper;
    import org.apache.ibatis.annotations.Select;

    import java.util.List;

    @Mapper
    public interface GeoMapper extends BaseMapper<Geo> { }
