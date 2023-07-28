package com.setu.splitwise.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserContribution {
    Long userId;
    Double contribution;
}
