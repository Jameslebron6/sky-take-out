package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags="C端购物车相关接口")
public class ShoppingCartController {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车,商品信息为: {}",shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("从购物车中减去商品")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("从购物车中删除商品的信息为：{}",shoppingCartDTO);
        shoppingCartService.deleteShoppingCart(shoppingCartDTO);
        return Result.success();
    }
    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list(){
        List<ShoppingCart> list=shoppingCartService.showShoppingcart();

        return Result.success(list);
    }
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

}
