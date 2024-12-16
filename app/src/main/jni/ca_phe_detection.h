#pragma once

#include <opencv2/core/core.hpp>

#include <net.h>
#include "sau_rieng_detection.h"


class ca_phe_detection {
public:
    ca_phe_detection();

    int load(AAssetManager *mgr);

    int detect(const cv::Mat &rgb, std::vector<Object> &objects, float prob_threshold = 0.3f,
               float nms_threshold = 0.45f);

    int predictPath(const std::string &imagePath, std::vector<Object> &objects,
                    float prob_threshold, float nms_threshold);


private:
    ncnn::Net yolo;
    int target_size;
    ncnn::UnlockedPoolAllocator blob_pool_allocator;
    ncnn::PoolAllocator workspace_pool_allocator;
};