import React from 'react';
import {
  Card,
  CardContent,
  Typography,
  Chip,
  Box,
  LinearProgress,
} from '@mui/material';

function GroupCard({ group }) {
  const memberProgress = (group.currentMembers / group.maxMembers) * 100;

  return (
    <Card sx={{ 
      minWidth: 275, 
      m: 2,
      transition: '0.3s',
      '&:hover': {
        transform: 'translateY(-5px)',
        boxShadow: 3
      }
    }}>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          {group.groupName}
        </Typography>
        <Typography color="text.secondary" gutterBottom>
          {group.festivalName}
        </Typography>
        <Typography variant="body2" sx={{ mb: 2 }}>
          {group.description}
        </Typography>
        <Box sx={{ mb: 2 }}>
          {group.tags.map((tag, index) => (
            <Chip 
              key={index} 
              label={tag} 
              size="small" 
              sx={{ mr: 1, mb: 1 }} 
            />
          ))}
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
          <Typography variant="body2" sx={{ mr: 1 }}>
            멤버 ({group.currentMembers}/{group.maxMembers})
          </Typography>
          <LinearProgress 
            variant="determinate" 
            value={memberProgress} 
            sx={{ flexGrow: 1 }}
          />
        </Box>
        <Typography variant="caption" color="text.secondary">
          그룹장: {group.leaderName} | 생성일: {group.createdAt}
        </Typography>
      </CardContent>
    </Card>
  );
}

export default GroupCard;
