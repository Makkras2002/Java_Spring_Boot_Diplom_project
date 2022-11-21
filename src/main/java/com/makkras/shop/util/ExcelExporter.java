package com.makkras.shop.util;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.exception.CustomServiceException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExcelExporter {

    void export(HttpServletResponse response, List<CompleteClientsOrder> clientsOrders) throws CustomServiceException;
}
