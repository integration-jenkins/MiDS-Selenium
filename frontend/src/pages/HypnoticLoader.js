import { useState, useEffect, useCallback } from 'react';
import { useSpring, animated } from '@react-spring/web';
import Particles from '@tsparticles/react';
import { loadFull } from 'tsparticles';
import styled, { keyframes } from 'styled-components';

const fractalGrowth = keyframes`
  0% { transform: scale(0) rotate(0deg); opacity: 0; }
  50% { transform: scale(1.2) rotate(180deg); opacity: 1; }
  100% { transform: scale(1) rotate(360deg); opacity: 0.8; }
`;

const quantumPulse = keyframes`
  0% { filter: hue-rotate(0deg); }
  100% { filter: hue-rotate(360deg); }
`;

const LoaderContainer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
`;

const Hexagon = styled(animated.div)`
  position: relative;
  width: 200px;
  height: 200px;
  background: linear-gradient(45deg, #a5b4fc, #c4b5fd);
  clip-path: polygon(50% 0%, 100% 25%, 100% 75%, 50% 100%, 0% 75%, 0% 25%);
  animation: ${fractalGrowth} 3s infinite cubic-bezier(0.4, 0, 0.2, 1),
            ${quantumPulse} 6s infinite linear;
`;

const ParticleCanvas = styled.div`
  position: absolute;
  width: 100%;
  height: 100%;
`;

const ProgressText = styled(animated.div)`
  position: absolute;
  font-size: 2rem;
  font-weight: bold;
  background: linear-gradient(45deg, #f472b6, #fbbf24);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  filter: drop-shadow(0 0 8px rgba(165, 180, 252, 0.5));
`;

const HypnoticLoader = ({ progress }) => {
  const [flip, setFlip] = useState(false);
  const { x } = useSpring({
    reset: true,
    reverse: flip,
    from: { x: 0 },
    x: 1,
    config: { duration: 3000 },
    onRest: () => setFlip(!flip),
  });

  const particlesInit = useCallback(async engine => {
    await loadFull(engine);
  }, []);

  const particlesConfig = {
    particles: {
      number: { value: 150 },
      color: { value: ['#a5b4fc', '#c4b5fd', '#f472b6'] },
      opacity: { value: 0.5, random: true },
      size: { value: 3, random: true },
      move: {
        enable: true,
        speed: 1.5,
        direction: 'none',
        outModes: 'bounce',
        trail: {
          enable: true,
          length: 10,
          fillColor: '#fff'
        }
      },
      links: {
        enable: true,
        distance: 150,
        color: '#c4b5fd',
        opacity: 0.4,
        width: 1
      },
      shape: {
        type: 'circle'
      }
    }
  };

  return (
    <LoaderContainer>
      <ParticleCanvas>
        <Particles init={particlesInit} options={particlesConfig} />
      </ParticleCanvas>
      
      <Hexagon style={{
        transform: x.to({
          range: [0, 0.25, 0.5, 0.75, 1],
          output: [1, 1.1, 0.9, 1.2, 1]
        }).to(x => `scale(${x})`)
      }}>
        <div className="absolute inset-0 flex items-center justify-center">
          <ProgressText>
            {Math.round(progress)}%
          </ProgressText>
        </div>
        
        <svg className="absolute inset-0">
          <defs>
            <linearGradient id="gradient" x1="0%" y1="0%" x2="100%" y2="100%">
              <stop offset="0%" style={{ stopColor: '#a5b4fc', stopOpacity: 1 }} />
              <stop offset="100%" style={{ stopColor: '#c4b5fd', stopOpacity: 1 }} />
            </linearGradient>
            <filter id="turbulence" x="0" y="0" width="100%" height="100%">
              <feTurbulence type="fractalNoise" baseFrequency="0.02" numOctaves="3" />
              <feDisplacementMap in="SourceGraphic" scale="15" />
            </filter>
          </defs>
          <circle cx="50%" cy="50%" r="40" fill="url(#gradient)" filter="url(#turbulence)" />
        </svg>
      </Hexagon>
    </LoaderContainer>
  );
};

export default HypnoticLoader;