package com.example.onboarding_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.onboarding_backend.entity.Geo;
import com.example.onboarding_backend.mapper.GeoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class GeoController {
    @Autowired
    private GeoMapper geoMapper;
    @GetMapping("/geo")
    public List<Geo> findAll()
    {
        List<Geo> geoList = geoMapper.selectList(null);
        System.out.println(geoList);
        return geoList;
    }

    @GetMapping("/geo/{id}")
    public Geo findSingle(@PathVariable("id")Integer id)
    {
        return geoMapper.selectById(id);
    }

    @GetMapping("/geo/search")
    public Geo searchBySuburb(@RequestParam("suburb") String suburb)
    {
        QueryWrapper<Geo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("suburb", suburb);
        return geoMapper.selectOne(queryWrapper);
    }

}
