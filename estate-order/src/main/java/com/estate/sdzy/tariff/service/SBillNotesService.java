package com.estate.sdzy.tariff.service;

import com.estate.sdzy.tariff.entity.SBillNotes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mq
 * @since 2020-09-21
 */
public interface SBillNotesService extends IService<SBillNotes> {
    List<SBillNotes> getAll(String token);
    List<SBillNotes> getByCommId(Long commId,String token);
    boolean insert(SBillNotes notes,String token);
    boolean update(SBillNotes notes,String token);
    boolean delete(Long id,String token);
}
