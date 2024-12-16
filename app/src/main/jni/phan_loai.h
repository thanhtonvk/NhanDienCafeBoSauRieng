//
// Created by TonSociu on 23/8/24.
//

#ifndef NHANDIENBENHCAYTRONG_PHAN_LOAI_H
#define NHANDIENBENHCAYTRONG_PHAN_LOAI_H

#include <net.h>
#include <opencv2/core/core.hpp>

class PhanLoai {
public:
    PhanLoai();

    int load(AAssetManager *mgr);

    int predict(cv::Mat src, std::vector<float> &result);

    int predictPath(const std::string &imagePath, std::vector<float> &result);

private:
    ncnn::Net model;
};

#endif //NHANDIENBENHCAYTRONG_PHAN_LOAI_H
