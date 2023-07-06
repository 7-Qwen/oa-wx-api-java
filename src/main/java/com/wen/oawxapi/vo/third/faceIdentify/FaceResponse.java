package com.wen.oawxapi.vo.third.faceIdentify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 7wen
 * @Date: 2023-07-05 20:29
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceResponse {
    private String message;
    private FaceResult result;
    private Long modelId;
}
