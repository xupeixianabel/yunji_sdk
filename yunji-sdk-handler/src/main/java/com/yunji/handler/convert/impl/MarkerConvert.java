package com.yunji.handler.convert.impl;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import com.yunji.handler.convert.IConverter;
import com.yunji.handler.vo.MarkerVO;
import com.yunji.handler.vo.OrientationVO;
import com.yunji.handler.vo.PoseVO;
import com.yunji.handler.vo.PositionVO;
/**
 * @author vincent
 *
 */
public class MarkerConvert implements IConverter<MarkerVO>
{

    public MarkerVO convert(JsonObject result)
    {
        MarkerVO markerVO = new MarkerVO();
        markerVO.setAvatar(result.getString("avatar"));
        markerVO.setFloor(result.getInt("floor"));
        markerVO.setKey(result.getInt("key"));
        markerVO.setMarkerName(result.getString("marker_name"));
        JsonObject pose = result.getJsonObject("pose");
        if (pose != null)
        {
            PoseVO poseVO = new PoseVO();
            markerVO.setPoseVO(poseVO);
            JsonObject postionJson = pose.getJsonObject("position");
            if (postionJson != null)
            {
                PositionVO positionVO = new PositionVO(postionJson);
                poseVO.setPositionVO(positionVO);
            }

            JsonObject orientationJson = pose.getJsonObject("orientation");
            if (orientationJson != null)
            {
                OrientationVO orientationVO = new OrientationVO(orientationJson);
                poseVO.setOrientationVO(orientationVO);
            }
        }
        return markerVO;
    }

    public MarkerVO convert(MarkerVO markVO, JsonObject result)
    {
        return null;
    }

    public List<MarkerVO> convertToList(JsonObject result)
    {
        JsonArray array = result.getJsonArray("markers");
        int size = array.size();
        List<MarkerVO> markerVOs = new ArrayList<MarkerVO>();
        for (int index = 0; index < size; index++)
        {
            MarkerVO markerVO = convert(array.getJsonObject(index));
            markerVOs.add(markerVO);
        }
        return markerVOs;
    }

}
