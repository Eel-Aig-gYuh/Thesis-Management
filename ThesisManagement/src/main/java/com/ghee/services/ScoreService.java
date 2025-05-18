/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services;

import com.ghee.dto.ScoreRequest;
import com.ghee.dto.ScoreResponse;

/**
 *
 * @author giahu
 */
public interface ScoreService {
    ScoreResponse createScore(long id, ScoreRequest dto, String username);
}
