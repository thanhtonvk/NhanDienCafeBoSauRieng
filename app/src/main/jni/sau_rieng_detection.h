#pragma once

#include <opencv2/core/core.hpp>

#include <net.h>

struct Object {
    cv::Rect_<float> rect;
    int label;
    float prob;
};

class sau_rieng_detection {
public:
    sau_rieng_detection();

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