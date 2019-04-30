package com.xunwei.som.base.controller;

import com.xunwei.som.base.model.Machine;
import com.xunwei.som.service.impl.MachineServiceImpl;
import com.xunwei.som.util.SOMUtils;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServiceBoardController
  extends BaseController
{
  private MachineServiceImpl machineServiceImpl = new MachineServiceImpl();
  
  @RequestMapping({"/serviceBoard/orderSituation"})
  public ModelAndView orderSituation(ModelAndView modelAndView)
  {
    modelAndView.setViewName("/serviceboard/html/orderSituation");
    return modelAndView;
  }
  
  @RequestMapping({"/serviceBoard/equipmentSituation"})
  public ModelAndView equipmentSituation(ModelAndView modelAndView)
  {
    modelAndView.setViewName("/serviceboard/html/equipmentSituation");
    return modelAndView;
  }
  
  @RequestMapping({"/serviceBoard/contractSituation"})
  public ModelAndView contractSituation(ModelAndView modelAndView)
  {
    modelAndView.setViewName("/serviceboard/html/contractSituation");
    return modelAndView;
  }
  
  @RequestMapping({"/serviceBoard/onTheJob"})
  public ModelAndView onTheJob(ModelAndView modelAndView)
  {
    modelAndView.setViewName("/serviceboard/html/onTheJob");
    return modelAndView;
  }
  
  @RequestMapping({"/serviceBoard/customerSatisfaction"})
  public ModelAndView customerSatisfaction(ModelAndView modelAndView)
  {
    modelAndView.setViewName("/serviceboard/html/customerSatisfaction");
    return modelAndView;
  }
  
  @RequestMapping(value={"/SearchWorkList"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  @ResponseBody
  public Object Login(ModelAndView modelAndView)
  {
    String viewName = "redirect:/serviceboard/html/serviceboard";
    String customerName = this.request.getParameter("customerName");
    if ((SOMUtils.isNull(customerName)) || (SOMUtils.isNull(customerName)))
    {
      modelAndView.setViewName(viewName);
      return modelAndView;
    }
    try
    {
      List<Machine> Machines = this.machineServiceImpl.selectMachineByDynamic(customerName);
      for (Machine machine : Machines) {
        System.out.println(machine);
      }
      return Machines;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return modelAndView;
  }
}
